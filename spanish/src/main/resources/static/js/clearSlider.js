/*
You will need a hidden text form field, with an id of #sliderValue that will get the
final value, and you will need a Range input with an id of "clearSlider". Something like
this:
<input id="sliderValue" type="hidden" name="anxiety"/>
<input id="clearSlider" type="range" min="0" max="100"/>
<input id="sliderEnabled" type="checkbox" value="555"/>

The final value is placed in the hidden attribute, so we can set it to 555 if prefer not to
answer is selected.
 */

$(document).ready(function () {

    $('#clearSlider').one('click', function (event) {
        setValue(event.pageX);
    });

    $('#clearSlider').one('touchstart', function(event) {
        setValue(event.originalEvent.touches[0].pageX);
    });

    $('#clearSlider').on('change', function(event) {
        $('#sliderValue').val(this.value).trigger('change');
    });

    function setValue(clickPoint) {
        var slider = $('#clearSlider');
        var x = clickPoint - slider.offset().left;
        var value = Math.round(x / slider.width() * 100);
        $('#clearSlider').val(value);
        $('#clearSlider').addClass('clicked');
        $('#sliderValue').val(value).trigger('change');
    }


    $('#sliderEnabled').click(function () {
        if (this.checked) {
            $('#clearSlider').attr("disabled", true);
            $('#clearSlider').removeClass("clicked");
            $('#sliderValue').val(555).trigger('change');
        } else {
            $('#clearSlider').attr("disabled", false);
            $('#clearSlider').addClass("clicked");
            $('#sliderValue').val($('#current_anxiety_slider').val()).trigger('change');
        }
    });
});

