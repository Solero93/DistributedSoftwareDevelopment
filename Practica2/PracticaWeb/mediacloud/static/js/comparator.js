function compare(ips) {
    $('#comparison').html(""); // Delete all content inside div comparison
    var finalHtml = "<h4>Comparación con otros mediacloud ordenado por precio</h4>";
    var selected = [];
    $('#checkBoxes input:checked').each(function () {
        selected.push($(this).attr('data-name'));
    });

    if (selected.length != 1) {
        alert("Select exactly ONE item to compare");
        return;
    }

    var item = selected[0];
    if (!item) {
        alert("Internal ERROR when comparing"); // Shouldn't arrive here
        return;
    }

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
                success: function(data){

                }
            })
        );
    }
    var itemList = [];
    $.when.apply($, requests).done(function () {
        console.log(arguments);
        var allItems;
        for (var i = 0; i < arguments.length; i+=2) {
            allItems = arguments[i][0]["results"];
            var found = false;
            var comments;
            for (var j = 0; j < allItems.length && !found; j++) {
                comments = arguments[i+1][0]["results"];
                if (allItems[j]["name"] === item) {
                    itemList.push([ips[i/2], allItems[j]["price"], allItems[j]["description"], allItems[j]["url"], comments]);
                    found = true;
                }
            }
        }
        itemList.sort(function f(a, b) {
            return (a[1] - b[1]);
        });

        var comments;
        for (var i = 0; i < itemList.length; i++) {
            finalHtml += "Ítem de: " + itemList[i][0] + "<br>" +
                "&#8195;Precio: " + itemList[i][1] + "<br>" +
                "&#8195;Descripción: " + itemList[i][2] + "<br>" +
                "&#8195;Comentarios: <br>";
                comments = itemList[i][4];
                for (var j=0; j<comments.length; j++){
                    if (comments[j]["item"] === itemList[i][3]) {
                        finalHtml += "&#8195;&#8195;" + comments[j]["text"] + "<br>";
                    }
                }
            finalHtml +="<br>"
        }
        $('#comparison').html(finalHtml);
    });
}