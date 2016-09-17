var serviceUri = "webapi/movies";

function doSearch(imdbId) {
    $.ajax({
        url: serviceUri + "/" + imdbId,
        type: 'GET',
        contentType: 'application/json'
    })
        .done(function (data, status, xhr) {
            appendElement(data);
        });
}

function appendElement(movieList) {
    for (var i = 0; i < movieList.length; i++) {
        var movie = movieList[i];
        var li = $("<li>")
            .append(
                $("<a>")
                    .text(movie.language + " " + movie.quality)
                    .attr("href", movie.source)
                    .attr("id", movie.id)
                    .attr("target", "_blank")
            );
        $("#movie-list")
            .append(li)
    }
}

///+++ on load +++

$(function () {

    $("#search-button").click(
        function () {
            $("#movie-list").empty();
            var value = $("#text-input").val();
            doSearch(value);
        });

});