from django.contrib.auth.decorators import login_required, permission_required
from django.contrib.auth.forms import UserCreationForm
from django.core.urlresolvers import reverse
from django.db.models import Sum
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.shortcuts import render

from mediacloud.models import Item, Types, Comment, Client

ips = ["161.116.52.35"] # "localhost","localhost","localhost"]


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


def error(request):
    context = {
    }
    return render(request, 'error.html', context)


def buy(request):
    items = []
    for id in request.session["selectedItems"]:
        items.append(Item.objects.get(pk=id))
    context = {
        'items': items
    }
    return render(request, 'buy.html', context)


@login_required
def bought(request):
    selectedItems = request.session["selectedItems"]
    price = Item.objects.filter(pk__in=selectedItems).aggregate(Sum('price'))
    if price["price__sum"] > request.user.client.money:
        return error(request)
    for i in request.session["selectedItems"]:
        request.user.client.itemsBought.add(i)
    return HttpResponseRedirect(reverse('download'))


@login_required
def download(request):
    context = {
        'items': request.user.client.itemsBought.all()
    }
    return render(request, 'download.html', context)


@login_required
def downloadFile(request, id):
    file = "mediacloud/downloads/algo.mp3"
    fsock = open(file)
    response = HttpResponse(fsock, content_type='audio/mpeg')
    response['Content-Disposition'] = "attachment; filename=" + str(file)
    return response


@login_required
@permission_required('mediacloud.write_comments', raise_exception=True)
def commentItem(request, id):
    textCom = ""
    rate = 3
    print request.user.get_all_permissions()
    print "hey ", ('mediacloud.write_comments' in request.user.get_all_permissions())

    try:
        textCom = request.POST['commentText']
        rate = request.POST['rate']
        Comment.objects.create(user=request.user, nick=request.user.get_username(), item=Item.objects.get(pk=id),
                               score=rate, text=textCom)
    except:
        return redirectToIndex()
    return HttpResponseRedirect(request.META.get('HTTP_REFERER', '/'))


def shoppingcart(request):
    selectedItems = []
    for key in request.POST:
        if key.startswith("checkbox"):
            selectedItems.append(request.POST[key])

    request.session["selectedItems"] = selectedItems
    return HttpResponseRedirect(reverse('buy'))


def redirectToIndex(request=None):
    return HttpResponseRedirect(reverse('index'))
