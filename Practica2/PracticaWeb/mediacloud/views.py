from django.contrib.auth.decorators import login_required, permission_required
from django.contrib.auth.forms import UserCreationForm
from django.core.urlresolvers import reverse
from django.db.models import Sum
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.shortcuts import render

from mediacloud.models import Item, Types, Comment, Client, Cart

ips = ["161.116.52.35"]  # "localhost","localhost","localhost"]


def register(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)
        if form.is_valid():
            new_user = form.save()
            Client.objects.create(user=new_user, money=100)
            return redirectToIndex()
    else:
        form = UserCreationForm()
    return render(request, "registration/register.html", {
        'form': form,
    })


def index(request):
    request.user.is_superuser
    types_catalog = Types.objects.all()
    context = {
        'types': types_catalog,
    }
    return render(request, 'index.html', context)


def catalog(request, type="all"):
    catalog_by_type = Item.objects.filter(type=type) if type != "all" and type != "" else Item.objects.all()
    types_catalog = Types.objects.all()
    context = {
        'catalog': catalog_by_type,
        'type': type,
        'types': types_catalog,
        'ips': ips
    }
    return render(request, 'catalog.html', context)


def detall(request, id):
    item_by_id = Item.objects.get(pk=id)

    comments_by_id = Comment.objects.filter(item__pk=id)

    context = {
        'item': item_by_id,
        'comments': comments_by_id,
        'idItem': id
    }

    return render(request, 'description.html', context)


def error(request, textError='404 Page not found'):
    context = {
        'textError': textError
    }
    return render(request, 'error.html', context)


def buy(request):
    items = []
    try:
        if request.session["cart"] == -1 or len(Cart.objects.get(pk=request.session["cart"]).itemList.all()) <= 0:
            return error(request, textError='Empty cart')
        for item in Cart.objects.get(pk=request.session["cart"]).itemList.all():
            items.append(item)
    except:
        request.session["cart"] = -1
        return error(request, textError='Empty cart')
    try:
        userItem = request.user.client.itemsBought.all()
    except:
        userItem = []
    context = {
        'items': items,
        'userItems': userItem
    }
    return render(request, 'buy.html', context)


@login_required
def bought(request):
    price = Cart.objects.get(pk=request.session["cart"]).itemList.all().aggregate(Sum('price'))

    if price["price__sum"] > request.user.client.money:
        return error(request, textError='Not enough money   ')
    for i in Cart.objects.get(pk=request.session["cart"]).itemList.all():
        try:
            request.user.client.itemsBought.add(i)
        except:
            return error(request, textError='You are not a normal user, you need to register again')
    Cart.objects.get(pk=request.session["cart"]).delete()
    userClient = request.user.client
    userClient.money = request.user.client.money - price["price__sum"]
    userClient.save()
    return HttpResponseRedirect(reverse('download'))


@login_required
def download(request):
    context = {
        'items': request.user.client.itemsBought.all()
    }
    return render(request, 'download.html', context)


@login_required
def profile(request):
    try:
        context = {
            'infoClient': request.user.client,
            'items': request.user.client.itemsBought.all()
        }
    except:
        return error(request, textError='You are not a normal user, you need to register again')
    return render(request, 'profile.html', context)


@login_required
def downloadFile(request, id):
    try:
        if not Item.objects.get(pk=id) in request.user.client.itemsBought.all():
            return error(request, textError="You can't download this")
    except:
        return  error(request, textError="This item doesn't exist")
    file = "mediacloud/downloads/algo.mp3"
    fsock = open(file)
    response = HttpResponse(fsock, content_type='audio/mpeg')
    response['Content-Disposition'] = "attachment; filename=" + str(file)
    return response


@login_required
@permission_required('mediacloud.write_comments', raise_exception=True)
def commentItem(request, id):
    try:
        textCom = request.POST['commentText']
        rate = request.POST['rate']
        Comment.objects.create(user=request.user, nick=request.user.get_username(), item=Item.objects.get(pk=id),
                               score=rate, text=textCom)
    except:
        return error(request, textError='Error commenting, comment again    ')
    return HttpResponseRedirect(request.META.get('HTTP_REFERER', '/'))


def shoppingcart(request):
    try:
        if request.session["cart"] != -1:
            carrito = Cart.objects.get(pk=request.session["cart"])
        else:
            carrito = Cart.objects.create()
            carrito.save()
            request.session["cart"] = carrito.pk
    except:
        carrito = Cart.objects.create()
        carrito.save()
        request.session["cart"] = carrito.pk

    for key in request.POST:
        if key.startswith("checkbox"):
            carrito.itemList.add(Item.objects.get(pk=request.POST[key]))
            carrito.save()

    return HttpResponseRedirect(reverse('buy'))


def removeItem(request, id):
    carrito = Cart.objects.get(pk=request.session["cart"])
    carrito.itemList.remove(Item.objects.get(pk=id))
    return HttpResponseRedirect(reverse('buy'))


def redirectToIndex(request=None):
    return HttpResponseRedirect(reverse('index'))
