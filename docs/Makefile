.ONESHELL: prepare

prepare:
		./prepare.sh

build:
	     docker run -v .:/srv/jekyll:Z -it jekyll/jekyll:4.2.2 jekyll build

serve: build
	     docker run -v ./_site:/usr/share/nginx/html:ro -p 8080:80  nginx