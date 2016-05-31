from django.contrib.auth.models import User
from rest_framework import serializers

from .models import Item, Comment


class ItemSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Item
        fields = ('url', 'description', 'name', 'type', 'price', 'img')


class CommentSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Comment
        fields = ('url', 'nick', 'item', 'score', 'text')


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('url', 'username')
