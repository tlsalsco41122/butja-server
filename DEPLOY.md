# Butja Server EC2 Docker Compose Deployment

이 문서는 AWS EC2 한 대에서 Spring Boot 백엔드와 MySQL을 Docker Compose로 실행하고, `main` 브랜치 push 시 GitHub Actions로 자동 배포하는 방법을 설명한다.

## 1. EC2 생성

권장 기본값:

- OS: Ubuntu 22.04 LTS 또는 Ubuntu 24.04 LTS
- Instance: t3.micro 이상
- Security Group inbound:
  - SSH: `22`
  - Backend: `8080`

HTTPS, Nginx, RDS, ECS, Docker Hub는 현재 단계에서 사용하지 않는다.

## 2. Docker 설치

EC2에 SSH 접속 후 실행한다.

```bash
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg git

sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

sudo usermod -aG docker ubuntu
```

권한 적용을 위해 SSH를 재접속한다.

## 3. Docker Compose 확인

```bash
docker --version
docker compose version
```

## 4. 프로젝트 최초 클론

배포 기준 경로는 `/home/ubuntu/butja-server`이다.

```bash
cd /home/ubuntu
git clone <YOUR_REPOSITORY_URL> butja-server
cd /home/ubuntu/butja-server
```

## 5. 환경변수 설정

`.env.example`을 복사해서 실제 값을 채운다.

```bash
cp .env.example .env
vi .env
```

필수 환경변수:

```env
DB_URL=jdbc:mysql://mysql:3306/butja?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=butja
DB_PASSWORD=
MYSQL_DATABASE=butja
MYSQL_ROOT_PASSWORD=
JWT_SECRET=
JWT_ISSUER=butja
JWT_ACCESS_EXP=86400000
JWT_REFRESH_EXP=604800000
PORT=8080
JPA_DDL_AUTO=update
```

`JWT_SECRET`은 Base64 문자열이어야 한다. 현재 JWT 코드가 Base64 디코딩 후 서명 키로 사용하기 때문이다.

## 6. 최초 배포

```bash
cd /home/ubuntu/butja-server

./gradlew clean build

docker compose up -d --build
```

상태 확인:

```bash
docker ps
docker compose logs -f
```

## 7. GitHub Secrets 설정

GitHub 저장소의 `Settings` → `Secrets and variables` → `Actions`에 아래 값을 등록한다.

```text
EC2_HOST
EC2_USERNAME
EC2_SSH_KEY
```

예시:

- `EC2_HOST`: EC2 public IP 또는 도메인
- `EC2_USERNAME`: `ubuntu`
- `EC2_SSH_KEY`: EC2 접속용 private key 전체 내용

## 8. 자동 배포 동작 방식

`.github/workflows/deploy.yml`은 `main` 브랜치 push 시 실행된다.

흐름:

```text
GitHub Push
→ GitHub Actions
→ EC2 SSH 접속
→ /home/ubuntu/butja-server 이동
→ git pull origin main
→ ./gradlew clean build
→ docker compose up -d --build
```

## 9. 운영 명령어

컨테이너 상태:

```bash
docker ps
```

로그 확인:

```bash
docker compose logs -f
docker compose logs -f backend
docker compose logs -f mysql
```

재시작:

```bash
docker compose restart
docker compose restart backend
```

중지:

```bash
docker compose down
```

다시 실행:

```bash
docker compose up -d
```

이미지 재빌드 후 실행:

```bash
./gradlew clean build
docker compose up -d --build
```

## 10. 데이터 유지

MySQL 데이터는 Docker volume `mysql_data`에 저장된다. 컨테이너를 재생성해도 데이터는 유지된다.

```bash
docker volume ls
```

`docker compose down -v`를 실행하면 volume까지 삭제되어 DB 데이터가 사라지므로 운영 환경에서는 주의한다.
