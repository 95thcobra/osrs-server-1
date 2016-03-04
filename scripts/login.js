var hometeleport = require('veteres/skills/magic/hometeleport.js');

/* Login script */

var PANE_FIXED = 548;

function initialize_player(player) {
    // Be polite
    player.message('Welcome to BartScape.');
}

/* Register the login event */
events.onLogin(initialize_player);
events.onButton(0, 548, 94, function(player) {
    player.message("You clicked the button :)");
});