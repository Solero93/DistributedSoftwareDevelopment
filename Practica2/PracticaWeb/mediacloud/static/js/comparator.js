function compare(ips) {
    $('#comparison').html(""); // Delete content
    var finalHtml = "<br><br>"
    var selected = [];
    $('#checkBoxes input:checked').each(function () {
        selected.push($(this).attr('value'));
    });

    if (selected.length != 1) {
        $('#comparison').html(finalHtml + "Select exactly ONE item to compare");
        return;
    }

    var itemId = selected[0];
    var item = null;
    // Get selected item's name from its ID
    $.when(
        $.ajax({
            url: "/api/items/" + itemId + "/",
        })
    ).done(function () {
        item = arguments[0]["name"];
        if (!item) {
            $('#comparison').html(finalHtml + "Internal ERROR"); // Shouldn't arrive here
            return;
        }

        var requests = [];
        for (var i = 0; i < ips.length; i++) {
            requests.push(
                $.ajax({
                    url: "http://" + ips[i] + ":8080/api/items/",
                })
            );
        }
        var itemList = [];
        $.when.apply($, requests).done(function () {
            var allItems;
            for (var i = 0; i < arguments.length; i++) {
                allItems = arguments[i][0]["results"];
                var found = false;
                for (var j = 0; j < allItems.length && !found; j++) {
                    if (allItems[j]["name"] === item) {
                        itemList.push([ips[i], allItems[j]["price"], allItems[j]["description"], allItems[j]["comments"]]);
                        found = true;
                    }
                }
                if (!found) {
                    itemList.push([ips[i], "NOT FOUND / ERROR OF WEBSERVER"]);
                }
            }
            itemList.sort(function f(a, b) {
                return (a[1] - b[1]);
            });

            for (var i = 0; i < itemList.length; i++) {
                finalHtml += "Ítem de: " + itemList[i][0] + " <br>" +
                    "&#8195;Precio: " + itemList[i][1] + "<br>" +
                    "&#8195;Descripción: " + itemList[i][2] + "<br>" +
                    "&#8195;Comentarios: " + itemList[i][3] + "<br>";
            }
            $('#comparison').html(finalHtml);
        });
    });
}