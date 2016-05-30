from __future__ import unicode_literals

import json

from django.contrib.auth.models import User
from django.db import models


class Types(models.Model):
    name = models.CharField(max_length=400)

    def __str__(self):
        return "" + self.name


class Item(models.Model):
    description = models.CharField(max_length=400)
    name = models.CharField(max_length=40)
    type = models.CharField(max_length=10)
    price = models.FloatField(default=0)
    img = models.CharField(max_length=400)

    def __str__(self):
        return "[" + self.type + "] " + self.name


class Cart(models.Model):
    foo = models.ManyToManyField(User, blank=True)

    def setfoo(self, x):
        self.foo = json.dumps(x)

    def getfoo(self):
        return json.loads(self.foo)


class Client(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    money = models.FloatField(default=0)
    itemsBought = models.ManyToManyField(Item, blank=True)

    def __str__(self):
        return "[" + self.user.get_username() + "] "


class Comment(models.Model):
    nick = models.CharField(max_length=40)
    user = models.ForeignKey(User)
    item = models.ForeignKey(Item)
    score = models.FloatField(default=0)
    text = models.CharField(max_length=601)

    class Meta:
        permissions = (
            ("write_comments", "Can write a comment"),
        )
