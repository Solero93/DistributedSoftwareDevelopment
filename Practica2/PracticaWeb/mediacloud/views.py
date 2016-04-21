from django.http import HttpResponse
from django.shortcuts import render

from mediacloud.models import Item


def index(request):
    return HttpResponse("Hello, world. You're at the mediacloud index.")


def catalog(request, type):
    catalog_by_type = Item.objects.filter(type=type)
    context = {
        'catalog': catalog_by_type,
        'type': type,
    }
    return render(request, 'catalog.html', context)
