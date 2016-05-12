from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^$', views.index, name='index'),

    url(r'^catalog/(.*)/(?P<id>.*)/$', views.detall, name='detall'),
    url(r'^register/$', views.register, name='register'),
    url(r'^login/$', views.error, name='login'),
    url(r'^logout/$', 'django.contrib.auth.views.logout', {'next_page': '/mediacloud/'}, name="logout"), # TODO fix this - doesn't redirect

    url(r'^catalog/(?P<type>.*)/$', views.catalog, name='catalog'),
    url(r'^download/(?P<id>.*)/$', views.downloadFile, name='downloadFile'),

    url(r'^catalog/.*$', views.catalog, name='catalog'),
    url(r'^buy/$', views.buy, name='buy'),
    url(r'^download/$', views.download, name='download'),
    url(r'^bought/$', views.bought, name='bought'),
    url(r'^shoppingcart/$', views.shoppingcart, name='shoppingcart'),
    url(r'^.*', views.error, name='error'),
]
