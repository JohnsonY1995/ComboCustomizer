$(function() {
    $('.list-group-sortable-connected').sortable({
        placeholderClass: 'list-group-item',
        connectWith: '.connected',
        items: 'li:not(.disabled)'
    });

    $("#testItem").on("drop", function(event) {
        event.preventDefault();
        event.stopPropagation();
        alert("Dropped!");
    });

    $('#testItem').on('dragover',function(event){
        event.preventDefault();
        alert('test');
    })
});

function allowDrop(x) { x.preventDefault(); }

function checkSelection() {
    var selections = '';
    $('#itemsChosen > li').each(function () {
        selections += $(this).attr('data-item') + ' ';
    });
    $('#selectionSummary').html(selections);
}

