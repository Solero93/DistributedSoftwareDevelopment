from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^$', views.index, name='index'),

    url(r'^catalog/.*/(?P<id>.*)/$', views.detall, name='detall'),
    url(r'^catalog/(?P<type>.*)/$', views.catalog, name='catalog'),
    url(r'^catalog/.*$', views.catalog, name='catalog'),
    url(r'^.*', views.error, name='error'),
]
