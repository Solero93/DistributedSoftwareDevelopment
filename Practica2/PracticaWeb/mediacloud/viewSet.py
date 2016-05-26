from django.contrib.auth.models import User, Group
from rest_framework import viewsets
from rest_framework.decorators import detail_route
from rest_framework.response import Response
from rest_framework import status

from mediacloud.permissions import IsExpertOrReadOnly
from .serializers import ItemSerializer, CommentSerializer, UserSerializer
from .models import Item, Comment


class ItemViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows Items to be viewed or edited.
    """
    queryset = Item.objects.all().order_by('name')
    serializer_class = ItemSerializer


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows Items to be viewed or edited.
    """
    queryset = User.objects.all().order_by('username')
    serializer_class = UserSerializer


class CommentViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows Comments to be viewed or edited.
    """
    queryset = Comment.objects.all().order_by('nick')
    serializer_class = CommentSerializer

    def create(self, request):

        if (not 'mediacloud.write_comments' in request.user.get_all_permissions()):
            return Response({'status': 'Not AN EXPERT USER'},status=status.HTTP_403_FORBIDDEN)

        serializerCom = CommentSerializer(data=request.data)
        if serializerCom.is_valid():
            print serializerCom.validated_data
            Comment.objects.create(user=request.user, nick=request.user.get_username(),
                                   item=serializerCom.validated_data['item'],
                                   score=serializerCom.validated_data['score'],
                                   text=serializerCom.validated_data['text'])

            return Response({'status': 'Comment set'})

        return Response(serializerCom.errors, status=status.HTTP_400_BAD_REQUEST)

    @detail_route(methods=['post'], permission_classes=[IsExpertOrReadOnly])
    def postComment(self, request, pk=None):

        if (not 'mediacloud.write_comments' in request.user.get_all_permissions()):
            return Response({'status': 'Not AN EXPERT USER'},status=status.HTTP_403_FORBIDDEN)

        serializerCom = CommentSerializer(data=request.data)
        if serializerCom.is_valid():
            print serializerCom.validated_data
            Comment.objects.create(user=request.user, nick=request.user.get_username(),
                                   item=serializerCom.validated_data['item'],
                                   score=serializerCom.validated_data['score'],
                                   text=serializerCom.validated_data['text'])

            return Response({'status': 'Comment set'})

        return Response(serializerCom.errors, status=status.HTTP_400_BAD_REQUEST)

    @detail_route(methods=['put'], permission_classes=[IsExpertOrReadOnly])
    def putComment(self, request, pk=None):

        if (not self.get_object().user == request.user):
            return Response({'status': 'Not your comment'},status=status.HTTP_403_FORBIDDEN)

        serializerCom = CommentSerializer(data=request.data)
        if serializerCom.is_valid():
            myObj = Comment.objects.get(pk=pk)
            myObj.text = serializerCom.validated_data['text']
            myObj.score = serializerCom.validated_data['score']
            myObj.save()

            return Response({'status': 'Comment put'})

        return Response(serializerCom.errors, status=status.HTTP_400_BAD_REQUEST)

    def update(self, request, pk=None):

        if not self.get_object().user == request.user:
            return Response({'status': 'Not your comment'},status=status.HTTP_403_FORBIDDEN)

        serializerCom = CommentSerializer(data=request.data)
        if serializerCom.is_valid():
            myObj = Comment.objects.get(pk=pk)
            myObj.text = serializerCom.validated_data['text']
            myObj.score = serializerCom.validated_data['score']
            myObj.save()

            return Response({'status': 'Comment put'})

        return Response(serializerCom.errors, status=status.HTTP_400_BAD_REQUEST)
