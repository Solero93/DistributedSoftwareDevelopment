function compare(ips, item) {
    $('#comparison').html(""); // Delete all content inside div comparison
    var finalHtml = "";

    var requests = [];
    for (var i = 0; i < ips.length; i++) {
        requests.push(
            $.ajax({
                url: "http://" + ips[i] + ":8080/api/items/",
            })
        );
        requests.push(
            $.ajax({
                url: "http://" + ips[i] + ":8080/api/comments/",
            })
        );
    }
    var itemList = [];
    $.when.apply($, requests).done(function () {
        var allItems;
        for (var i = 0; i < arguments.length; i += 2) {
            allItems = arguments[i][0]["results"];
            var found = false;
            var comments;
            for (var j = 0; j < allItems.length && !found; j++) {
                comments = arguments[i + 1][0]["results"];
                if (allItems[j]["name"] === item) {
                    itemList.push([ips[i / 2], allItems[j]["price"], allItems[j]["description"], allItems[j]["url"], comments]);
                    found = true;
                }
            }
        }
        itemList.sort(function f(a, b) {
            return (a[1] - b[1]);
        });

        var comments;
        for (var i = 0; i < itemList.length; i++) {

            finalHtml += '<div class="descripObj"><br><h2><b>Ítem de</b>:</h2><i>' + itemList[i][0] + "</i>"+
                "<br><h2><b>Precio</b>:</h2><i>" + itemList[i][1] + "</i>" +
                "<br><h2><b>Descripción</b>:</h2><i>" + itemList[i][2] + "</i>" +
                    "<br><h2><b>Comentarios</b>:</h2><div class='comment'>";
            comments = itemList[i][4];
            for (var j = 0; j < comments.length; j++) {
                if (comments[j]["item"] === itemList[i][3]) {
                    console.log(comments[j]["text"] );
                    finalHtml +=   comments[j]["text"] + "<br>";
                }
            }
            finalHtml += "</div><br>"
        }
        $('#comparison').html(finalHtml);
    });
}