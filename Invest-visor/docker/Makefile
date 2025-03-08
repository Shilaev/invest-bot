# Указываем файл .env
ENV_FILE=.env
ifneq ("$(wildcard $(ENV_FILE))","")
    include $(ENV_FILE)
endif

start-db:
	docker compose -f docker-compose.yml up invest-visor-db flyway -d
