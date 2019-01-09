/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team6679.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot
{
    // Initialize a Joystick (Logitech Extreme 3D Pro) controller to control the robot
    private Joystick primaryController = new Joystick(0);

    // Initialize the drivetrain motors
    private WPI_TalonSRX leftDriveMotor1;
    private WPI_TalonSRX leftDriveMotor2;
    private WPI_TalonSRX rightDriveMotor1;
    private WPI_TalonSRX rightDriveMotor2;

    // Initialize the arm motor
    private WPI_TalonSRX armMotor;

    // Initialize the ramp motor
    private WPI_TalonSRX rampMotor;

    // Pairs up the drivetrain motors based on their respective side and initializes the drivetrain controlling object
    private SpeedControllerGroup leftSideDriveMotors;
    private SpeedControllerGroup rightSideDriveMotors;
    private DifferentialDrive robotDrive;


    // Function run once when the robot is turned on
    @Override
    public void robotInit()
    {
        // Assigns all the motors to their respective objects (the number in brackets is the port # of what is connected where)
        leftDriveMotor1 = new WPI_TalonSRX(2);
        leftDriveMotor2 = new WPI_TalonSRX(3);
        rightDriveMotor1 = new WPI_TalonSRX(0);
        rightDriveMotor2 = new WPI_TalonSRX(1);
        armMotor = new WPI_TalonSRX(4);
        rampMotor = new WPI_TalonSRX(5);

        // Assigns the drivetrain motors to their respective motor controller group and then passes them on to the drivetrain controller object
        leftSideDriveMotors = new SpeedControllerGroup(leftDriveMotor1, leftDriveMotor2);
        rightSideDriveMotors = new SpeedControllerGroup(rightDriveMotor1, rightDriveMotor2);
        robotDrive = new DifferentialDrive(leftSideDriveMotors, rightSideDriveMotors);

        // Sets the appropriate configuration settings for the motors
        leftSideDriveMotors.setInverted(true);
        rightSideDriveMotors.setInverted(true);
        robotDrive.setSafetyEnabled(true);
        robotDrive.setExpiration(0.1);
        robotDrive.setMaxOutput(0.80);
        armMotor.setSafetyEnabled(false);
        rampMotor.setSafetyEnabled(true);
    }

    // Function run in an endless loop during the teleop mode
    @Override
    public void teleopPeriodic()
    {
        // Button 3 - Moves the arms closed to release the hatch panel
        if (primaryController.getRawButton(3))
        {
            armMotor.set(-0.60);
        }
        // Button 4 - Moves the arms open to grab the hatch panel
        else if (primaryController.getRawButton(4))
        {
            armMotor.set(0.60);
        }
        // Stops the arm motor if neither Button 3 or Button 4 were pressed
        else
        {
            armMotor.set(0);
        }

        // Button 1 (Trigger) - Passes on the input from the primary controller's speed slider to move the ramp vertically
        if (primaryController.getRawButton(1))
        {
            rampMotor.set(primaryController.getRawAxis(3));
        }
        // Stops the ramp motor from moving if Button 1 (Trigger) was not pressed
        else
        {
            rampMotor.set(0);
        }

        // Sends the Y axis input (speed) and the X axis input (rotation) from the primary controller's stick to move the robot
        robotDrive.arcadeDrive(primaryController.getY(), -primaryController.getX());
    }
}
