from rest_framework import viewsets

from .models import Item, Comment
from .serializers import ItemSerializer, CommentSerializer


class ItemViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows Items to be viewed or edited.
    """
    queryset = Item.objects.all().order_by('name')
    serializer_class = ItemSerializer


class CommentViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows Comments to be viewed or edited.
    """
    queryset = Comment.objects.all().order_by('nick')
    serializer_class = CommentSerializer
