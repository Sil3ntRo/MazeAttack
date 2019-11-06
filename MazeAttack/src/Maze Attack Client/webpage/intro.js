function Intro()
{

var circles = [], post, target, vel, spring, speed, disc, wasd, spacebar;
var canvas, rotation;
var initials = "";


  this.setup = function(){

    canvas = createCanvas(windowWidth - 10, windowHeight - 10);

    background(255);
    disc = loadImage('images/disc.png');
    wasd = loadImage('images/wasd.jpg');
    spacebar = loadImage('images/spacebar.jpg');
  	translate(width/2, height/2);
    pos = new p5.Vector(0,0),
  	target = new p5.Vector(0,0),
  	vel = new p5.Vector(0,0),
  	spring= 0.97,
  	speed = 0.03;
    rotation = 0;

    var noOfCircles = 15;

  	for (var i=0; i< noOfCircles; i++) {
  	  var offsetAngle = (2*PI)/noOfCircles;
  		var angle = offsetAngle;
  		var radius = width/7;

  		target = new p5.Vector(radius*sin(angle*i),radius*cos(angle*i));
  		circle = new Circle(pos.x, pos.y, target);
  		circles.push(circle);

  	}
  }

  this.draw = function(){
  push();
    angleMode(DEGREES);
  	fill(255);
    noStroke();
    translate(width/3, height/2);

    rect(-windowWidth/2,-windowHeight/2, windowWidth*2, windowHeight);
    rotation = rotation + 1;
    rotate(rotation);

  		for(var i =0; i < circles.length; i ++) {
  			var p = circles[i];
  			target.set(p.target.x, p.target.y);
  			pos.set(p.posX, p.posY);
  			vel.set(p.velX, p.velY);
  			vel.mult(spring);

  			var diff = p5.Vector.sub(target, pos);
  			diff.mult(speed);
  			vel.add(diff);
  			pos.add(vel);

  			p.posX = pos.x;
  			p.posY = pos.y;
  			p.velX = vel.x;
  			p.velY = vel.y;

  			p.render();
  	 	}
      imageMode(CENTER);
      image(disc,0,0);
    pop();

    push();
    strokeWeight(3);
    push();
    textAlign(RIGHT);
    textSize(55);
    text("Maze Escape", width-width/10, height/5.2);
    pop();
    textSize(35)
    fill('blue');
    text("Enter Initials", width-width/3.3, height/3.3);
    fill('black')
    text("_____", width-width/3.6, height/2.7);
    text(initials.toUpperCase(), width-width/3.6, height/2.7);
    line(width-width/2.75, height/5, width-width/10, height/5); // top
    line(width-width/2.75, height-height/5, width-width/2.75, height/5); // left
    line(width-width/2.75, height-height/5, width-width/10, height-height/5); // bottom
    line(width-width/10, height/5, width-width/10, height-height/5); // right
    image(wasd, width-width/3.3, height/2.4, width/8, height/6);
    image(spacebar, width-width/3.3, height-height/2.5, width/8, height/6);
    textSize(15);
    textAlign(CENTER);
    fill('red');
    text("Press enter to start", width-width/4.3, height-height/6);
    pop();
  }

  this.keyPressed = function(){
    if(keyCode == 13){ // ENTER
      document.getElementById('initialElement').innerHTML = initials;
      this.sceneManager.showScene( Game );
    }
    if(keyCode == 8){ // BACKSPACE
      initials = initials.substring(0, initials.length - 1)
    }


  }

  this.keyTyped = function(){
    if(initials.length < 3){
      initials += key;

    }
  }



  function Circle(posx, posy,t) {

  	this.posX = posx;
  	this.posY = posy;

  	this.target = new p5.Vector(0,0,0);
  	this.target.set(t);
  	this.velX = 0;
  	this.velY = 0;
  	this.size = 50;

    this.red = random(255);
    this.green = random(255);
    this.blue = random(255);

  	this.render = function() {
      stroke(0);
      strokeWeight(3);
      fill(this.red, this.green, this.blue);
  		ellipse(this.posX, this.posY, this.size,this.size);
  	}
  }
}
