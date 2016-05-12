python manage.py makemigrations mediacloud
python manage.py migrate
python manage.py loaddata fixtures/*.json
google-chrome http://localhost:8080/mediacloud/
python manage.py runserver 0.0.0.0:8080