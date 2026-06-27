# Запуск проекта

Репозиторий дипломного проекта по теме: "Разработка клиент-серверного приложения для подготовки к собеседованиям"

Состав:

- `prep-auth` - сервис авторизации, внешний адрес `http://localhost:8083`
- `prep-assessment` - основной backend, внешний адрес `http://localhost:8080`
- `prep-front-two` - frontend, внешний адрес `http://localhost:5173`
- `starter-template` - общий Maven starter, отдельно запускать не нужно
- PostgreSQL - внешний порт `55432`
- MinIO - API `http://localhost:59000`, консоль `http://localhost:59001`

## Что установить

На компьютере нужны:

1. Git
2. Docker Desktop

Java, Maven и Node.js для Docker-запуска устанавливать не нужно: сборка идет внутри контейнеров.

Проверка:

```bash
git --version
docker --version
docker compose version
```

## Первый запуск

Склонируйте репозиторий и зайдите в его папку:

```bash
git clone <ссылка-на-репозиторий>
cd <название-склонированной-папки>
```

Запустите все сервисы:

```bash
docker compose up --build -d
```

После запуска откройте:

```text
http://localhost:5173
```

Демо-администратор создается автоматически, если в `.env` включен bootstrap:

```text
login: demo-admin
password: demo123
```

О bootstrap:

- пользователь создается только если пользователя с таким login еще нет
- если пользователь уже есть, ему выдается роль администратора
- если volume базы уже содержит этого администратора, повторное создание не выполняется
- если нужно создать demo-администратора заново на чистой БД, удалите volumes командой `docker compose down -v`

## Проверка контейнеров

```bash
docker compose ps
```

Все сервисы должны быть в состоянии `running` или `healthy`.

Логи всех сервисов:

```bash
docker compose logs -f
```

Логи конкретного сервиса:

```bash
docker compose logs -f prep-auth
docker compose logs -f prep-assessment
docker compose logs -f prep-frontend
```

## Переменные окружения

Основные настройки лежат в `.env`. Файл `.env.example` оставлен как образец, если нужно восстановить значения по умолчанию.

В `.env` уже предусмотрены:

- `JWT_PRIVATE_KEY` и `JWT_PUBLIC_KEY` - ключи для токенов
- `DB_NAME`, `DB_USER`, `DB_PASS`, `DB_PORT` - настройки PostgreSQL
- `MINIO_ROOT_USER`, `MINIO_ROOT_PASSWORD`, `MINIO_BUCKET` - настройки MinIO
- `AUTH_PORT`, `ASSESSMENT_PORT`, `FRONTEND_PORT` - внешние порты приложения
- `VITE_AUTH_BASE_URL`, `VITE_ASSESSMENT_BASE_URL` - backend-адреса, которые попадут во frontend при сборке
- `BOOTSTRAP_ADMIN_ENABLED`, `BOOTSTRAP_ADMIN_LOGIN`, `BOOTSTRAP_ADMIN_PASSWORD`, `BOOTSTRAP_ADMIN_EMAIL` - автосоздание demo-администратора

Если меняете `AUTH_PORT` или `ASSESSMENT_PORT`, поменяйте также соответствующие `VITE_*` переменные и пересоберите frontend:

```bash
docker compose up --build -d prep-frontend
```

## Адреса после запуска

```text
Frontend:        http://localhost:5173
Auth backend:    http://localhost:8083
Assessment API:  http://localhost:8080
MinIO console:   http://localhost:59001
```

MinIO:

```text
login: minio
password: minio123
```

## Остановка

Остановить контейнеры:

```bash
docker compose down
```

Остановить и удалить данные PostgreSQL/MinIO:

```bash
docker compose down -v
```

`down -v` удаляет данные, поэтому для обычной остановки используйте просто `docker compose down`.

## Частые проблемы

Если Docker пишет ошибку про daemon, откройте Docker Desktop и дождитесь запуска.

Если порт занят, измените порт в `.env` или остановите программу, которая уже использует этот порт. Например, если занят порт MinIO `9000`, поставьте:

```text
MINIO_PORT=59000
MINIO_CONSOLE_PORT=59001
```

Если Docker build упал на Maven с ошибкой скачивания зависимости, например `Illegal packet size`, обычно достаточно повторить:

```bash
docker compose up --build -d
```

Если ошибка повторяется, очистите Docker build cache и запустите сборку еще раз:

```bash
docker builder prune
docker compose up --build -d
```

Если frontend открылся, но запросы падают, проверьте значения:

```text
VITE_AUTH_BASE_URL=http://localhost:8083
VITE_ASSESSMENT_BASE_URL=http://localhost:8080
```

После изменения этих переменных нужен повторный build:

```bash
docker compose up --build -d
```
