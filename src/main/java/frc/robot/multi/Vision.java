package frc.robot.multi;

import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.MultiTargetPNPResult;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.drive.DriveSubSystem;

public class Vision {
    
    private static PhotonPoseEstimator estimator = new PhotonPoseEstimator(
        AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded),
        PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
        new Transform3d(
            new Translation3d(
                Units.inchesToMeters(13),
                0,
                Units.inchesToMeters(4)
            ),
            new Rotation3d(
                0,
                Math.PI / 6,
                0
            )
        )
    );

    // TODO: cam name
    private static PhotonCamera cam = new PhotonCamera("name");

    public static void run() {

        List<PhotonPipelineResult> results = cam.getAllUnreadResults();

        if (results.size() == 0) { return; }

        PhotonPipelineResult result = results.get(results.size() - 1);

        if (result.targets.size() < 2) {
            return;
        }

        Optional<EstimatedRobotPose> pose = estimator.update(result);

        if (pose.isEmpty()) { return; }

        Pose2d pose2d = pose.get().estimatedPose.toPose2d();

        SmartDashboard.putNumber("visX", pose2d.getX());
        SmartDashboard.putNumber("visY", pose2d.getY());
        SmartDashboard.putNumber("visR", pose2d.getRotation().getDegrees());

        DriveSubSystem.INSTANCE.chassis.resetPose(pose2d);

        GlobalResources.setGyroAngle(pose2d.getRotation());
    }
}
