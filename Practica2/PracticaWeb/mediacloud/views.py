from django.contrib import auth
from django.contrib.auth.decorators import login_required
from django.http import HttpResponseRedirect, HttpResponse
from django.core.urlresolvers import reverse
from django.shortcuts import render

from mediacloud.models import Item, Types, comment, cart, Client
from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.http import HttpResponseRedirect
from django.shortcuts import render

def register(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)
        if form.is_valid():
            new_user = form.save()
            Client.objects.create(user=new_user)
            return HttpResponseRedirect("/mediacloud")
    else:
        form = UserCreationForm()
    return render(request, "registration/register.html", {
        'form': form,
    })



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
    items=[]

    for id in request.session["selectedItems"] :
        items.append(Item.objects.get(pk=id))
    context = {
        'items': items
    }
    return render(request, 'buy.html', context)

@login_required
def bought(request):
    print request.user.get_username()
    print Client.objects.all()
    for i in request.session["selectedItems"]:
        request.user.client.itemsBought.add(i)
    request.session["selectedItems"]=[]
    return HttpResponseRedirect(reverse('download'))

@login_required
def download(request):
    context = {
        'items': request.user.client.itemsBought.all()
    }
    return render(request, 'download.html', context)

@login_required
def downloadFile(request, id):
    file="mediacloud/downloads/algo.mp3"
    fsock = open(file)
    response = HttpResponse(fsock, content_type ='audio/mpeg')
    response['Content-Disposition'] = "attachment; filename=" + str(file)
    return response

def shoppingcart(request):
    selectedItems = []
    for key in request.POST:
        if key.startswith("checkbox"):
            selectedItems.append(request.POST[key])

    request.session["selectedItems"] = selectedItems
    return HttpResponseRedirect(reverse('buy'))

def redirectToIndex(request):
    return HttpResponseRedirect(reverse('index'))
