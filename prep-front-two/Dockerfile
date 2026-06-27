FROM node:22-alpine AS build

WORKDIR /app

ARG VITE_AUTH_BASE_URL=http://localhost:8083
ARG VITE_ASSESSMENT_BASE_URL=http://localhost:8080

COPY package.json package-lock.json ./
RUN npm ci \
    --fetch-retries=5 \
    --fetch-retry-factor=2 \
    --fetch-retry-mintimeout=20000 \
    --fetch-retry-maxtimeout=120000 \
    --prefer-offline

COPY . .

ENV VITE_AUTH_BASE_URL=${VITE_AUTH_BASE_URL}
ENV VITE_ASSESSMENT_BASE_URL=${VITE_ASSESSMENT_BASE_URL}

RUN npm run build

FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

