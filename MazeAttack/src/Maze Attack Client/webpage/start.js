function setup()
{
    createCanvas(windowWidth - 10, windowHeight - 10);

    var mgr = new SceneManager();
    mgr.addScene ( Intro );
    mgr.addScene ( Game );
    mgr.wire();
    mgr.showScene( Intro );

}
