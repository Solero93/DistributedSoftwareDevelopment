from __future__ import unicode_literals

from django.db import models
import json
from django.contrib.auth.models import User


class Item(models.Model):
    description = models.CharField(max_length=400)
    name = models.CharField(max_length=40)
    type = models.CharField(max_length=10)
    price = models.FloatField(default=0)
    img = models.CharField(max_length=400)


    def __str__(self):
        return "[" + self.type + "] " + self.name


class Types(models.Model):
    name = models.CharField(max_length=400)

    def __str__(self):
        return "" + self.name

class comment(models.Model):
    idItem= models.IntegerField(default=0)
    nick = models.CharField(max_length=40)
    score= models.FloatField(default=0)
    text= models.CharField(max_length=600)

    def __str__(self):
        return "[" + self.nick + "] " + self.idItem

class cart(models.Model):
    foo = models.CharField(max_length=200,default="[]")

    def setfoo(self, x):
        self.foo = json.dumps(x)

    def getfoo(self):
        return json.loads(self.foo)



class Client(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    money=models.FloatField(default=0)
    itemsBought=models.ManyToManyField(Item, blank=True)