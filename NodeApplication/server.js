var express = require('express'),
  app = express(),
  port = Number(process.env.PORT || 8080);

// ROUTES
// ===============================================

// Define the home page route.
app.get('/', function(req, res) {
  res.send('Our first route is working.:)');
});

// START THE SERVER
// ===============================================

app.listen(port, function() {
  console.log('Listening on port ' + port);
});
