
/**
 * Created by Bart on 2/15/2015.
 *
 * Simple function that handles the home teleport spell.
 * @param player Player, passed form rsjs.
 */
(function(exp) {

    /* Function which does the home teleporting. */
    function teleport(player) {
        /* Do we have any preconditions? */
        if ('requirements' in exp) {
            for (key in exp.requirements) {
                if (exp.requirements[key](player) === false)
                    return;
            }
        }

        // The sequence in [anim, gfx, delay] format.
        var sequence = [{anim: 4847, gfx: 800, delay: 7},
            {anim: 4850, delay: 4},
            {anim: 4853, gfx: 802, delay: 3},
            {anim: 4855, gfx: 803, delay: 4},
            {anim: 4857, gfx: 804, delay: 3}];

        // Execute each part of the sequence...
        for (var i in sequence) {
            var seq = sequence[i];

            player.animate(seq.anim);
            if ('gfx' in seq)
                player.graphic(seq.gfx);
            player.delay(seq.delay);
        }

        // ... and teleport our player.
        player.teleport(3221, 3217, 0);
        player.animate(-1);
        player.graphic(-1);
    }

    /* Register the event trigger, only when we're first evaluated. */
    events.onButton(0, 192, 0, teleport);

})(typeof exports !== "undefined" ? exports : require(''));