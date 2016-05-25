from django.conf.urls import url

from . import views

listOfAddresses = ["161.116.56.65", "161.116.56.165"]

urlpatterns = [
    url(r'^$', views.index, name='index'),

    url(r'^catalog/(.*)/(?P<id>.*)/$', views.detall, name='detall'),
    url(r'^commentItem/(?P<id>.*)/$', views.commentItem, name='commentItem'),
    url(r'^register/$', views.register, name='register' ),

    url(r'^catalog/(?P<type>.*)/$', views.catalog, name='catalog'),
    url(r'^download/(?P<id>.*)/$', views.downloadFile, name='downloadFile'),

    url(r'^comparator/.*$', views.comparator, {'ips': listOfAddresses}, name='comparator'),
    url(r'^catalog/.*$', views.catalog, name='catalog'),
    url(r'^buy/$', views.buy, name='buy'),
    url(r'^download/$', views.download, name='download'),
    url(r'^bought/$', views.bought, name='bought'),
    url(r'^shoppingcart/$', views.shoppingcart, name='shoppingcart'),

    url(r'^.*', views.error, name='error'),
]
