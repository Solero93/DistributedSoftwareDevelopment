from django.contrib.auth.models import User, Group
from rest_framework import viewsets
from .serializers import ItemSerializer
from .models import Item

class ItemViewSet(viewsets.ModelViewSet):
            """
            API endpoint that allows Items to be viewed or edited.
            """
            queryset = Item.objects.all().order_by('name')
            serializer_class = ItemSerializer