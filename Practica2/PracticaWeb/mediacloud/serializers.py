from django.contrib.auth.models import User

from .models import Item, Comment
from rest_framework import serializers


class ItemSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Item
        fields = ('description', 'name', 'type', 'price', 'img')


class CommentSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Comment
        fields = ('pk', 'nick', 'item', 'score', 'text')