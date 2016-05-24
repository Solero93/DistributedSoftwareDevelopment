"""PracticaWeb URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.9/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url, include
from django.contrib import admin
from django.contrib.auth.views import login, logout
from rest_framework import routers
from mediacloud import viewSet

router = routers.DefaultRouter()
router.register(r'items', viewSet.ItemViewSet)
router.register(r'comments', viewSet.CommentViewSet)

urlpatterns = [
    url(r'^mediacloud/login/$', login, name="login"),
    url(r'^mediacloud/logout/$',logout, {'next_page': '/mediacloud/'}, name="logout"),
    url(r'^mediacloud/', include('mediacloud.urls')),
    url(r'^admin/', admin.site.urls),
    url(r'^api/', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]
