function Game()
{

var socket;
var user;
var users = [];
var walls = [];
var npcDiscs = [];
var npcDiscImg;
var laser;
var arc;

var width;
var height;
var canvas;

var input;
var typing;
var inputWidth;

var mouseAngle;
var targetAngle;
var easing;
var laserXwall;
var laserYwall;

var chatTimeDisplay;
var chatResetTimer



  this.setup = function(){
      // Connecting to server
      socket = new WebSocket("ws://127.0.0.1:9999");
      user = new User();
      user.initials = document.getElementById('initialElement').innerHTML;
      socket.onmessage = e => this.messageHandler(e);
      socket.onopen = () => this.sendMsgToServer();

      // canvas setup
      width = windowWidth - 10;
      height = windowHeight - 10
      canvas = createCanvas(width, height);
      frameRate(60);

      // chat setup
      typing = false;
      input = createInput('');
      inputWidth = 600;
      input.size(inputWidth, 25);
      input.position(width/2 - (inputWidth/2), height - (height*0.08));
      input.mousePressed(this.typing);


      // Angle for arrow
      angle = 0;

      // Setup
      laser = new Laser();
      chatTimeDisplay = 6;
      this.resetChatTimer();
      npcDiscImg = loadImage('images/disc.png');

  }

  this.draw = function(){
    background(255);
    textAlign(CENTER);
    text("(SPACEBAR TO SHOOT LASER!)", width/2, height - (height*0.02));
    textAlign(LEFT);
    //scale(0.3);
    var oldx = user.x;
    var oldy = user.y;
    this.checkMovement();

    // Mouse pointer section
    mouseAngle = atan2( mouseY - height/2, mouseX - width/2 );

    push();
    // Adjust canvas so user is always in the centre position
    translate(width/2, height/2);
    translate(-user.x, -user.y);


    // If spacebar is on, draw laser and if it hits wall, reduce length to impact intersection
    if(laser.on == true){
      for(i = 0; i < walls.length; i++){
        var hitWall = this.lineLine(walls[i].x1,walls[i].y1, walls[i].x2, walls[i].y2, laser.x1, laser.y1, laser.x2, laser.y2);
        if(hitWall == true){
          laser.x2 = laserXwall;
          laser.y2 = laserYwall;
        }
      }
      laser.show();
    }

    if(user.id != ""){
      this.sendMsgToServer();
    }

    // Draw own user and chat
    user.show();
    textAlign(CENTER);
    strokeWeight(2);
    stroke(0)
    fill(255);
    textSize(15);
    text(user.initials, user.x, user.y + 5);
    if(user.lastSaid !== ""){
      chatResetTimer--;
      if(chatResetTimer > 0){
        strokeWeight(2)
        stroke(0)
        textAlign(CENTER);
        textSize(30);
        fill(user.red,user.green,user.blue);
        text(user.lastSaid, user.x, user.y-user.r*1.5);

      } else {
        user.lastSaid = "";
        this.sendMsgToServer();
        this.resetChatTimer();
      }
    }

    // Draw other users & their chat
    for (var i = users.length - 1; i >= 0; i--) {
      var id = users[i].userId;
        if (id != user.id) {
          if(users[i].laserOn == true){
            strokeWeight(5)
            stroke(users[i].red,users[i].green,users[i].blue,150);
            line(users[i].x, users[i].y, users[i].laserX, users[i].laserY);
          }
          strokeWeight(2)
          stroke(0)
          fill(users[i].red,users[i].green,users[i].blue);
          ellipse(users[i].x, users[i].y, users[i].r * 2, users[i].r * 2);
          textAlign(CENTER);
          textSize(30);
          text(users[i].lastSaid, users[i].x, users[i].y-users[i].r * 1.5);
          fill(255);
          textSize(15);
          text(users[i].initials, users[i].x, users[i].y + 5);
        }
    }

    // Draw walls & check user position collisons against walls
    strokeWeight(3)
    for(i = 0; i < walls.length; i++){
      line(walls[i].x1, walls[i].y1, walls[i].x2, walls[i].y2);
      var hit = this.lineCircle(walls[i].x1,walls[i].y1, walls[i].x2, walls[i].y2, user.x, user.y, user.r)
      if(hit == true){
        user.x = oldx;
        user.y = oldy;
      }
    }

    // Draw discs
    if(npcDiscs){
      for(i = 0; i < npcDiscs.length; i++){
          imageMode(CENTER);
          image(npcDiscImg, npcDiscs[i].x, npcDiscs[i].y);
          var hitDisc = this.lineCircle(laser.x1, laser.y1, laser.x2, laser.y2, npcDiscs[i].x, npcDiscs[i].y, npcDiscs[i].dia / 2);
          var hitUser = this.circleCircle(npcDiscs[i].x, npcDiscs[i].y, npcDiscs[i].dia / 2, user.x, user.y, user.r);
          if(hitUser == true){
            user.x = 100;
            user.y = -241;
            this.sendMsgToServer();
          }
          if(hitDisc == true && laser.on == true){
            user.hitDiscId = npcDiscs[i].discId;
            this.sendMsgToServer();
            user.hitDiscId = null;
            npcDiscs[i].x = 10000;
            npcDiscs[i].y = 10000;
          }

      }
    }



    pop();

    // Draw the triangle used to show aiming location
    push();
    translate(width/2, height/2);
    rotate(mouseAngle);
    triangle(user.r * 2, 0, user.r * 1.1 ,0 - (user.r * 0.5), user.r * 1.1, 0 + (user.r * 0.5));
    pop();


  }



  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // USER INPUT SECTION
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    this.checkMovement = function(){
    if(typing){
      // stops movement until message is sent
    } else {
      if (keyIsDown(87)) { // W
         user.y = user.y - 5;
         this.sendMsgToServer();
      }
      if (keyIsDown(83)) { // S
        user.y = user.y + 5;
        this.sendMsgToServer();
      }
      if (keyIsDown(65)) { // A
        user.x = user.x - 5;
        this.sendMsgToServer();
      }
      if (keyIsDown(68)) { // D
        user.x = user.x + 5;
        this.sendMsgToServer();
      }
      if (keyIsDown(32)) { // SPACE
        laser.on = true;
        laser.update();
      }
    }
  }

  this.keyReleased = function(){
    if (keyCode == 32) { // SPACE
      laser.on = false;
      this.sendMsgToServer();

    }
  }

  this.keyPressed = function(){
    if(keyCode == 13){ // ENTER
      if(input.value() == ""){
        input.elt.focus();
        typing = true;
      } else {
        this.submitChat()
      }
    }
  }

  this.typing = function(){
      typing = true;
  }

  /*
   * Client server messaging 
   */
  this.submitChat = function(){
    user.lastSaid = input.value();
    this.sendMsgToServer();
    input.value("");
    document.activeElement.blur();
    typing = false;
    this.resetChatTimer();
  }

  this.resetChatTimer = function(){
    chatResetTimer = 60 * chatTimeDisplay;
  }

  this.sendMsgToServer = function(){
    var data = {
      x: user.x,
      y: user.y,
      initials: user.initials,
      lastSaid: user.lastSaid,
      hitDiscId: user.hitDiscId,
      laserX: laser.x2,
      laserY: laser.y2,
      laserOn: laser.on
    }
    socket.send(JSON.stringify(data));
  }

  this.messageHandler = function(e){
    data = JSON.parse(e.data);

    if(data[0].msgType == "initUser"){
      user.id = data[0].myId;
      user.red = data[0].red;
      user.blue = data[0].blue;
      user.green = data[0].green;
    }
    if(data[0].msgType == "createMaze"){
      walls = data;
    }

    if(data[0].msgType == "relay"){
      users = data;
    }

    if(data[0].msgType == "npcDisc"){
      npcDiscs = data;
    }

    if(data[0].msgType == "createWalls"){
      var temp = data;
      for(i = 0; i < temp.length; i++){
        walls.push(temp[i]);
      }
    }
    //console.log(data)
  }

  /**
   * Collision Detection
   */
  this.lineCircle = function(x1, y1, x2, y2, cx, cy, r){
    var inside1 = this.pointCircle(x1,y1, cx,cy,r);
    var inside2 = this.pointCircle(x2,y2, cx,cy,r);
    if (inside1 || inside2) return true;
    var distX = x1 - x2;
    var distY = y1 - y2;
    var len = sqrt( (distX*distX) + (distY*distY) );
    var dot = ( ((cx-x1)*(x2-x1)) + ((cy-y1)*(y2-y1)) ) / pow(len,2);
    var closestX = x1 + (dot * (x2-x1));
    var closestY = y1 + (dot * (y2-y1));
    var onSegment = this.linePoint(x1,y1,x2,y2, closestX,closestY);
    if (!onSegment) return false;
    distX = closestX - cx;
    distY = closestY - cy;
    var distance = sqrt( (distX*distX) + (distY*distY) );
    if (distance <= r) {
      return true;
    }
    return false;
  }

  this.pointCircle = function(px, py, cx, cy, r) {
    var distX = px - cx;
    var distY = py - cy;
    var distance = sqrt( (distX*distX) + (distY*distY) );
    if (distance <= r) {
      return true;
    }
    return false;
  }

  this.linePoint = function(x1, y1, x2, y2, px, py) {
    var d1 = dist(px,py, x1,y1);
    var d2 = dist(px,py, x2,y2);
    var lineLen = dist(x1,y1, x2,y2);
    var buffer = 0.1;
    if (d1+d2 >= lineLen-buffer && d1+d2 <= lineLen+buffer) {
      return true;
    }
    return false;
  }

  this.lineLine = function(x1, y1, x2, y2, x3, y3, x4, y4) {
    // calculate the distance to intersection point
    var uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
    var uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));

    // if uA and uB are between 0-1, lines are colliding
    if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {

      // intersection (used for reducing laser length)
      laserXwall = x1 + (uA * (x2-x1));
      laserYwall = y1 + (uA * (y2-y1));
      return true;
    }
    return false;
  }

  this.circleCircle = function (c1x, c1y, c1r, c2x, c2y, c2r) {
    var distX = c1x - c2x;
    var distY = c1y - c2y;
    var distance = sqrt( (distX*distX) + (distY*distY) );

    // if the distance is less than the sum of the circle's radius, the circles are touching!
    if (distance <= c1r+c2r) {
      return true;
    }
    return false;
  }

  class User {
    constructor(){
      this.id = "";
      this.initials = "";
      this.x = 100;
      this.y = -241;
      this.red = 255;
      this.green = 255;
      this.blue = 255;
      this.r = 25;
      this.lastSaid = "";
      this.hitDiscId = null;
    }

    show(){
      strokeWeight(3)
      fill(this.red,this.green,this.blue);
      ellipse(this.x, this.y, this.r*2, this.r*2);
    }

    collides(wall){
      
      if(
        this.x + this.r >= wall.x &&
        this.x + this.r <= (wall.x + wall.wallWidth) &&
        this.y + this.r >= wall.wallY &&
        this.y + this.r <= (wall.wallY + wall.wallHeight)){
          return true;
        } else{
          return false;
        }
    }
  }

  class Laser {
    constructor(){
      this.on = false;
      this.x1 = user.x;
      this.y1 = user.y;
      //this.x2 = mouseX + width/2;
      this.x2 = mouseX;
      this.y2 = mouseY;
    }

    update(){
      // refreshing position
      this.x1 = user.x;
      this.y1 = user.y;
      this.x2 = (mouseX - width/2 + user.x);
      this.y2 = (mouseY - height/2 + user.y);

      // extending the laser length past mouse
      var i = 0;
      for(i = 0; i < 3; i++){
        this.x2 = this.x2 + (this.x2 - this.x1)
        this.y2 = this.y2 + (this.y2 - this.y1)
      }
    }

    show(){
      push();
      strokeWeight(5)
      stroke(user.red, user.green, user.blue, 150);
      line(this.x1, this.y1, this.x2, this.y2);
      pop();

    }
  }
}
