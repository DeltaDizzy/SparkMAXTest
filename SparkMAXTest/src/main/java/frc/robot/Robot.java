/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private Joystick stick;
  private CANSparkMax motor;
  private int sparkID = 0; // TODO: find this out
  ShuffleboardTab sparkTab;
  NetworkTableEntry nteCANError;
  NetworkTableEntry nteSparkRPM;
  NetworkTableEntry nteSparkTargetRPM;
  NetworkTableEntry nteSparkIdleMode;
  NetworkTableEntry nteSparkRampRate;
  private int current_rpm = 0;
  private int target_rpm = 0;
  private double ramp_rate = 3.0;
  private String tabName = "Spark MAX";

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() 
  {
    stick = new Joystick(0);
    motor = new CANSparkMax(sparkID, MotorType.kBrushless);
    sparkTab = Shuffleboard.getTab(tabName);

    // config Shuffleboard
    nteSparkRPM = sparkTab.add("Current Output", current_rpm).withProperties(Map.of("min", 0)).getEntry();
    nteSparkIdleMode = sparkTab.add("Idle Mode", IdleMode.kCoast.toString()).getEntry();
    nteSparkTargetRPM = sparkTab.add("Target RPM", target_rpm).getEntry();
    nteCANError = sparkTab.add("CAN Status", CANError.kOk).getEntry();
    nteSparkRampRate = sparkTab.add("Ramp Rate", ramp_rate).getEntry();

    // config spark
    motor.restoreFactoryDefaults();
    if (motor.setIdleMode(IdleMode.kCoast) != CANError.kOk) 
    {
      nteSparkIdleMode.setString("Error");
    }

    if (motor.getIdleMode() == IdleMode.kCoast) 
    {
      nteSparkIdleMode.setString("Coast");  
    }
    else
    {
      nteSparkIdleMode.setString("Brake");
    }

    // RAMP RATE
    if (motor.setOpenLoopRampRate(ramp_rate) != CANError.kOk) 
    {
      nteSparkRampRate.setString("Error");
    }
    else
    {
      nteSparkRampRate.setNumber(motor.getOpenLoopRampRate());
    }
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() 
  {
    motor.set(stick.getY());
    nteSparkRPM.setNumber(motor.getAppliedOutput());
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
