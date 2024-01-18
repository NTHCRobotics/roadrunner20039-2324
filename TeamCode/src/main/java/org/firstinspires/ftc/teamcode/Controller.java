package org.firstinspires.ftc.teamcode;




import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp
public class Controller extends OpMode{




















    private final ElapsedTime runtime = new ElapsedTime();




    //declaring motes




    private DcMotorEx wheelFL;
    private DcMotorEx wheelBL;
    private DcMotorEx wheelFR;
    private DcMotorEx wheelBR;




    private DcMotorEx armSlideMoter;




    private DcMotor intakeMoter;




    private Servo armServo;




    private CRServo planeServ;




















    private double previousRunTime;
    private double rotation;




    //intake on bool
    private boolean intakeOn = false;
    private boolean aPressed;








    double initialposition;
    private int minTickSlide = -20;
    private  int maxTickSlide = 2700;




    private int slideTarget;
    private boolean armUp = false;




    private boolean armflip = false;
    private  boolean bpressed = false;




    private boolean xPressed;




    double servpos = .3;
    double lerpPos;




    boolean planeServo;




    public void init(){




        telemetry.addData("Status", "Initialized");




        planeServ = hardwareMap.get(CRServo.class,"planeserv");
        //setting moters to where they are in driver controller config.
        wheelFL = hardwareMap.get(DcMotorEx.class, "wheelFL");
        wheelFR = hardwareMap.get(DcMotorEx.class, "wheelFR");
        wheelBL = hardwareMap.get(DcMotorEx.class, "wheelBL");
        wheelBR = hardwareMap.get(DcMotorEx.class, "wheelBR");




        armSlideMoter = hardwareMap.get(DcMotorEx.class, "slideMoter");




        armServo = hardwareMap.get(Servo.class, "servoArm");




        //adding intake moters.
        intakeMoter = hardwareMap.get(DcMotorEx.class, "IntaMoter");








        //allowing it to run withought encoders
        wheelFL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        wheelFR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        wheelBL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        wheelBR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        armSlideMoter.setTargetPosition(0);
        armSlideMoter.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);








        intakeMoter.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);




























        //setting makes some moters reversed due to their diffrent orintations of the robot.
        wheelFL.setDirection(DcMotorSimple.Direction.REVERSE);
        wheelFR.setDirection(DcMotorSimple.Direction.FORWARD);
        wheelBL.setDirection(DcMotorSimple.Direction.FORWARD);
        wheelBR.setDirection(DcMotorSimple.Direction.REVERSE);




        intakeMoter.setDirection(DcMotorSimple.Direction.FORWARD);




        armSlideMoter.setDirection(DcMotorSimple.Direction.REVERSE);








        //tell driver initialization if complete - do not add vulgarity @isaiah @saiansh
        telemetry.addData("Status","Initialized");








        initialposition = armSlideMoter.getCurrentPosition();
        minTickSlide = (int)(10 + initialposition);
        maxTickSlide = (int)(2700 + initialposition);








    }
    @Override
    public void init_loop(){} //runs after init but berfore play




    public void start(){
        runtime.reset();
        previousRunTime = getRuntime(); //mabey a bug but found it like this
    }












    @Override
    public void loop(){
        basicDrive();
        intake();
        armSlide();
        armflip();
        armflip();
        //    planeControl();
        telemetry.addData("Status",armflip);
    }




//    private void planeControl(){
//        if(gamepad1.x){
//            if(xPressed){
//
//
//            } else {
//                xPressed = true;
//                planeServo = !planeServo;
//            }
//        } else{
//            xPressed = false;
//        }
//        if(planeServo){
//            planeServ.setPower(-1);} else {
//            planeServ.setPower(0);
//        }
//
//
//    }




    private void armflip() {
        if(gamepad1.b){
            if(bpressed){




            } else {
                bpressed = true;
                armflip = !armflip;
                telemetry.addData("Status","Flip");
            }
        } else {
            bpressed = false;
        }








        if(armflip && !(slideTarget < 500)){
            armServo.setPosition(.08);
        } else {
            if(slideTarget < 500){
                armflip = false;
            }


            armServo.setPosition(.5);








        }




    }




    public void basicDrive(){
        //sets the speed of the robot based on how far the joystick is pushed
        double setSpeed = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);




        double moveAngle = Math.atan2(gamepad1.left_stick_x, gamepad1.left_stick_y) - Math.PI / 4;
        double turnAmount = gamepad1.right_stick_x;
        rotation += 1 * turnAmount;




        final double fLPower = setSpeed * (Math.cos(moveAngle  + 90)) - turnAmount;
        final double fRPower = setSpeed * (Math.sin(moveAngle  + 90)) + turnAmount;
        final double bLPower = setSpeed * (Math.sin(moveAngle  + 90)) - turnAmount;
        final double bRPower = setSpeed * (Math.cos(moveAngle  + 90)) + turnAmount;








        wheelFL.setPower(-fLPower);
        wheelFR.setPower(-fRPower);
        wheelBL.setPower(bLPower);
        wheelBR.setPower(bRPower);




    }




    public void intake(){
        if(gamepad1.a){


            if(aPressed){




            } else{
                aPressed = true;
                intakeOn = !intakeOn;
            }
        } else {
            aPressed = false;
        }

        if(gamepad1.x){
            intakeMoter.setDirection(DcMotorSimple.Direction.REVERSE);
        } else {
             intakeMoter.setDirection(DcMotorSimple.Direction.FORWARD);
        }




        if(intakeOn){
            intakeMoter.setPower(.4f);
        } else {
            intakeMoter.setPower(0);
        }
    }




    void armSlide(){
        //trigger imput








        armSlideMoter.setTargetPositionTolerance(20);
        if(gamepad1.left_bumper){
            slideTarget = 0;
        } else if(gamepad1.right_bumper) {
            slideTarget = 2700;
        }




        slideTarget += (int)((gamepad1.right_trigger * 25) - (gamepad1.left_trigger * 25));




        if(slideTarget > maxTickSlide) {
            slideTarget = maxTickSlide;
        } else if(slideTarget < minTickSlide){
            slideTarget = minTickSlide;
        }




        armSlideMoter.setTargetPosition(slideTarget + 100);




        if(armSlideMoter.getCurrentPosition() - slideTarget < 10 && armSlideMoter.getCurrentPosition() - slideTarget > 10){
            armSlideMoter.setVelocity(0);
        } else {
            armSlideMoter.setVelocity(1750);
        }












    }
































    public static void wait(int ms) {
        try {
            Thread.sleep(ms); //core java delay command
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); //this exception is useful to remove the glitches and errors of the thread.sleep()
        }
    }




    //@Override
    void Stop(){
    }
































}

