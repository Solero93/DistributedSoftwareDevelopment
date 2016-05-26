python manage.py dumpdata auth auth.Group contenttypes.Contenttype --indent 4 > fixtures/auth.json
python manage.py dumpdata mediacloud.client --indent 4 > fixtures/clients.json
python manage.py dumpdata mediacloud.item --indent 4 > fixtures/items.json
python manage.py dumpdata mediacloud.types --indent 4 > fixtures/itemTypes.json
python manage.py dumpdata mediacloud.comment --indent 4 > fixtures/itemComments.json
