from django.http import HttpResponse
from django.http import HttpResponseRedirect, HttpResponse
from django.core.urlresolvers import reverse
from django.shortcuts import render

from mediacloud.models import Item, Types, comment


def index(request):
    # TODO Hacer tabla de types
    catalog_by_type = Types.objects.all()
    context = {
        'types': catalog_by_type,
    }
    return render(request, 'index.html', context)


def catalog(request, type="all"):
    catalog_by_type = Item.objects.filter(type=type) if type != "all" and type != "" else Item.objects.all()
    context = {
        'catalog': catalog_by_type,
        'type': type,
    }
    return render(request, 'catalog.html', context)


def detall(request, id):
    item_by_id = Item.objects.get(pk=id)
    comments_by_id = comment.objects.filter(idItem=id)
    context = {
        'item': item_by_id,
        'comments': comments_by_id
    }
    return render(request, 'description.html', context)


def error(request):
    context = {
    }
    return render(request, 'error.html', context)
def buy(request):
    context = {
    }
    return render(request, 'buy.html', context)


def shoppingcart(request):
    selectedItems = []
    for key in request.POST:
        if key.startswith("checkbox"):
            selectedItems.append(request.POST[key])
    request.session["selectedItems"] = selectedItems
    return HttpResponseRedirect(reverse('mediacloud/buy'))
