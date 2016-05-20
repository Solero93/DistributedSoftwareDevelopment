python manage.py dumpdata auth mediacloud.client --indent 4 > fixtures/users.json
python manage.py dumpdata mediacloud.item mediacloud.types --indent 4 > fixtures/items.json
python manage.py dumpdata mediacloud.comment --indent 4 > fixtures/comments.json
