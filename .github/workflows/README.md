## 📌 CI/CD 개요
이 프로젝트는 **GitHub Actions + Docker + Nginx + Blue-Green Deployment**를 사용하여 **자동화된 CI/CD**를 수행합니다.

### ✅ CI (Continuous Integration)
- `kock-api`를 빌드하고 Docker 이미지를 생성하여 **Docker Hub에 푸시**합니다.

### ✅ CD (Continuous Deployment)
| 브랜치 | 동작                                            |
|--------|-----------------------------------------------|
| `develop` | **개발 서버** 배포 (`kok-dev-CD.yml`)               |
| `main` | **운영 서버 (Blue-Green 배포)** (`kok-prod-CD.yml`) |
| `kok-api` | **API 빌드 & Docker Push** (`kok-CI.yml`)       |

---

## 📂 CI/CD 워크플로우 파일 설명

| 파일명                     | 설명                       |
|-------------------------|--------------------------|
| `kok-CI.yml`            | `kok-api` 빌드 및 Docker 배포 |
| `kok-dev-CD.yml`        | 개발 서버 배포                 |
| `kok-prod-CD.yml`       | 운영 서버 (Blue-Green) 배포    |
| `blue-green-Nginx.conf` | Nginx 설정 (트래픽 스위칭)       |

---

## 🔑 **필요한 GitHub Secrets 목록**
| 이름 | 설명 |
|------|------|
| `DOCKERHUB_USERNAME` | Docker Hub 로그인 ID |
| `DOCKERHUB_PASSWORD` | Docker Hub 로그인 비밀번호 |
| `NCP_HOST` | NCP 서버 주소 (배포 서버 IP) |
| `NCP_USER` | NCP 서버 로그인 계정 (보통 `ubuntu` 또는 `root`) |
| `NCP_KEY` | NCP 서버 접근을 위한 SSH Key (pem 파일 내용) |
| `COMPOSE_FILE_PATH` | Docker Compose가 실행될 서버 경로 |
| `IMAGE_NAME` | Docker Hub에 업로드할 이미지명 |

---

## 🛠️ GitHub Actions 실행 방법

### 1️⃣ **수동 실행 (`workflow_dispatch`)**
GitHub Actions에서 `Run Workflow` 버튼을 눌러 배포할 수 있습니다.

### 2️⃣ **자동 실행 (브랜치 Push)**
| 브랜치       | 실행되는 워크플로우                   |
|-----------|------------------------------|
| `*`       | `kok-CI.yml` (빌드 & Docker 배포) |
| `develop` | `kok-dev-CD.yml` (개발 서버 배포)  |
| `main`    | `kok-prod-CD.yml` (운영 서버 배포) |

---

## 🚀 운영(Prod) 배포 프로세스 (Blue-Green Deployment)
운영 배포는 **Blue-Green Deployment** 방식을 사용하여 **무중단 배포**를 수행합니다.

### **1️⃣ 배포 트리거**
- `main` 브랜치에 코드가 푸시되면 `kok-prod-CD.yml`이 실행됩니다.
- GitHub Actions가 `docker-compose-prod.yml`을 서버에 업로드하고 배포를 시작합니다.

### **2️⃣ 현재 실행 중인 서비스 확인**
- `nginx.conf`에서 현재 사용 중인 컨테이너(`kok-blue` 또는 `kok-green`을 확인)합니다.

### **3️⃣ 새로운 컨테이너 실행**
- 현재 사용 중인 환경이 `kok-blue`이면 `kok-green`을 실행 (`docker-compose-green.yml` 사용).
- 현재 사용 중인 환경이 `kok-green`이면 `kok-blue`를 실행 (`docker-compose-blue.yml` 사용).

### **4️⃣ Health Check**
- 새롭게 실행된 컨테이너가 정상적으로 작동하는지 `/health_check` 엔드포인트를 통해 확인합니다.
- Health Check 실패 시 롤백하여 기존 컨테이너를 유지합니다.

### **5️⃣ 트래픽 전환**
- `nginx.conf`의 `upstream` 설정을 변경하여, 새로운 컨테이너로 트래픽을 전환합니다.
- `nginx -s reload` 명령어를 실행하여 Nginx설정을 반영합니다.

### **6️⃣ 기존 컨테이너 정리**
- 새로운 환경이 정상적으로 배포된 후, 이전 컨테이너를 중지하고 삭제하여 리소스를 절약합니다.

---

## 🚀 개발(Dev) 배포 프로세스
개발 배포는 운영 환경보다 **간단한 단일 컨테이너 배포** 방식으로 진행됩니다.

### **1️⃣ 배포 트리거**
- `develop` 브랜치에 코드가 푸시되면 `kok-dev-CD.yml`이 실행됩니다.
- GitHub Actions가 `docker-compose-dev.yml`을 서버에 업로드하고 배포를 시작합니다.

### **2️⃣ 기존 컨테이너 종료**
- 기존에 실행 중인 `kok-dev` 컨테이너를 중지 (`docker compose down` 실행).

### **3️⃣ 새로운 컨테이너 실행**
- `docker compose -f docker-compose-dev.yml up -d` 명령어를 실행하여 새로운 컨테이너를 실행합니다.