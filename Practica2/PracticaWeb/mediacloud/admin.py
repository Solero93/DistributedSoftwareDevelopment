from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
from django.contrib.auth.models import User

from .models import Item, Comment, Client


# Define an inline admin descriptor for Employee model
# which acts a bit like a singleton
class ClientInline(admin.StackedInline):
    model = Client
    can_delete = False
    verbose_name_plural = 'Clients'


# Define a new User admin
class UserAdmin(BaseUserAdmin):
    inlines = (ClientInline,)


# Re-register UserAdmin
admin.site.unregister(User)
admin.site.register(User, UserAdmin)
admin.site.register(Item)
admin.site.register(Client)
admin.site.register(Comment)
