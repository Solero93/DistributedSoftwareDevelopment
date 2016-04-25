from django.http import HttpResponse
from django.shortcuts import render

from mediacloud.models import Item, Types


def index(request):
    #TODO Hacer tabla de types
    catalog_by_type = Types.objects.all()
    context = {
        'types': catalog_by_type,
    }
    return render(request, 'index.html', context)


def catalog(request, type):
    catalog_by_type = Item.objects.filter(type=type)
    context = {
        'catalog': catalog_by_type,
        'type': type,
    }
    return render(request, 'catalog.html', context)
