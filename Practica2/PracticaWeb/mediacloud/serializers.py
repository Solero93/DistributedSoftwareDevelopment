from django.contrib.auth.models import User

from .models import Item, Comment
from rest_framework import serializers


class ItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = Item
        fields = ('description', 'name', 'type', 'price', 'img')


class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = ('pk', 'nick', 'item', 'score', 'text', 'user')
class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('pk', 'username')

