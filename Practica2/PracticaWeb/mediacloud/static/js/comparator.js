function compare(ips){
    $('#comparison').html(""); // Delete content

    var item = $('input[name=radioItem]:checked').val();
    if (!item) return; // Nothing is selected

    var requests = [];
    for (var i=0; i<ips.length; i++) {
        requests.push(
            $.ajax({
                url: "http://" + ips[i] + ":8080/api/items/",
            })
        );
    }
    var itemList = [];
    $.when.apply($, requests).done(function () {
        var allItems;
        for (var i=0; i<arguments.length; i++){
            allItems = arguments[i][0]["results"];
            var found = false;
            for (var j=0; j<allItems.length && !found; j++) {
                if (allItems[j]["name"] === item){
                    itemList.push([ips[i], allItems[j]["price"]]);
                    found = true;
                }
            }
            if (!found) {
                itemList.push([ips[i], "No se encontró"]);
            }
        }
        itemList.sort(function f(a,b) {
           return (a[1] - b[1]);
        });

        var finalHtml = "";
        for (var i=0; i<itemList.length; i++) {
            finalHtml += "Ítem de: " + itemList[i][0] + " --- Precio: " + itemList[i][1] + "<br>";
        }

        $('#comparison').html(finalHtml);
    });
}