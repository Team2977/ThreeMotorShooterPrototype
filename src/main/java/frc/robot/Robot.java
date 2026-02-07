// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import java.net.SocketOption;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.XboxController;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
// import com.revrobotics.spark.*;


/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
 
  // This is the motor. Uses a device ID of 6
  private final TalonFX shooterMotor1 = new TalonFX(18);
  private final TalonFX shooterMotor2 = new TalonFX(17);
  private final TalonFX intakeMotor = new TalonFX(19);
  // This is the controller. Should be in port 0 on the driverstation
  private final Joystick gamepad1 = new Joystick(0);
  
  
  private double setSpeed = 0;
  
private XboxController controller = new XboxController(0);
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code. ``12w111
   */



  public Robot() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

  }

  
  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // This is a TalonFX config object. There are many different configerations in here, including invert, neutral mode, 
    // and various PID and motion magic things
    TalonFXConfiguration config = new TalonFXConfiguration();

    // This sets the neutral mode of the motor. Meaning what the motor does when it doesn't have a commad. 
    // Coast will allow the motor to be spun in any which way without resistance
    // Brake will resist all movement of the motor. useful for a climber if you want the robot to maintain position after the match is over
    shooterMotor1.setNeutralMode(NeutralModeValue.Coast);
  
    intakeMotor.setNeutralMode(NeutralModeValue.Coast);
  

// shooter2.restoreFactoryDefaults();
// shooter2.setIdleMode(IdleMode.kCoast);
// shooter2.setInverted(false); // flip to true if needed


    // This is the new and better way to invert the motor.
    // It takes the TalonFX config object, and sets the inverted value to whatever you want it to be.
    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    // This is how you tell the motor to use the config we set up above.
    shooterMotor1.getConfigurator().apply(config, 1);
    
    intakeMotor.getConfigurator().apply(config, 1);
  }
  

  public void runIntake(double speed) {
    intakeMotor.set(speed);
    }

  public void runShooter(double speed) {
      shooterMotor1.set(speed);
      shooterMotor2.set(speed);
    }

  public void stopAll() {
      intakeMotor.stopMotor();
      shooterMotor1.stopMotor();
      shooterMotor2.stopMotor();
    }



  /** This function is called periodically during operator control. */
  /* */ @Override
  public void teleopPeriodic() {
    //shooter.set(MathUtil.applyDeadband(-gamepad1.getRawAxis(5), .05));
    
    
    // The following is a if-else chain that sets the motor speed based on the button pressed. 
  if (gamepad1.getRawButton(1)) {
  setSpeed = 0.7;
} else if (gamepad1.getRawButton(2)) {
  setSpeed = 0.8;
} else if (gamepad1.getRawButton(3)) {
  setSpeed = 0.9;
} else if (gamepad1.getRawButton(4)) {
  setSpeed = 1.0;
} else if (gamepad1.getRawButton(5)) {
  setSpeed = 0.0;
}

shooterMotor1.set(setSpeed);
shooterMotor2.set(0.8);
intakeMotor.set(setSpeed);

  
    // This is how we send numbers to a dashboard on the computer to see values mid match. 
    SmartDashboard.putNumber("shooter1 RPM", shooterMotor1.getVelocity().getValueAsDouble());
    //SmartDashboard.putNumber("shooter2 RPM", shooter2.().getVelocity().getValueAsDouble());
    SmartDashboard.putNumber("shooter3 RPM", intakeMotor.getVelocity().getValueAsDouble());
    SmartDashboard.putNumber("motor set speed", setSpeed);


    double lt = controller.getLeftTriggerAxis();
double rt = controller.getRightTriggerAxis();

// Intake motor (LT)
if (lt > 0.2) {
    intakeMotor.set(0.5);
} else {
    intakeMotor.set(0);
}

// Shooter motors (RT)
if (rt > 0.2) {
    shooterMotor1.set(1.0);
    shooterMotor2.set(1.0);
} else {
    shooterMotor1.set(0);
    shooterMotor2.set(0);
}

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
