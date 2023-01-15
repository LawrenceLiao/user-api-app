SHELL := /bin/bash
.PHONY: app_local_compose_up app_local_compose_down app_local_compose_start app_local_compose_stop \
		app_local_build app_local_run app_local_test app_docker_build

PROJECT       	?= userapi
TAG_VERSION 	?= snapshot

app_local_compose_up:
	docker-compose up -d

app_local_compose_down:
	docker-compose down

app_local_compose_start:
	docker-compose start

app_local_compose_stop:
	docker-compose stop

app_local_build:
	./gradlew clean build

app_local_run:
	./gradlew bootRun --args="--spring.profiles.active=local"

app_local_test:
	./gradlew clean test

app_docker_build:
	docker build . --tag ${PROJECT}:${TAG_VERSION}


