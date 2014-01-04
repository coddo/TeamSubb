package com.coddotech.teamsubb.main;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.connection.LoginWindow;
import com.coddotech.teamsubb.jobs.CreateJobWindow;
import com.coddotech.teamsubb.jobs.Job;
import com.coddotech.teamsubb.jobs.JobManager;
import com.coddotech.teamsubb.jobs.JobWindow;
import com.coddotech.teamsubb.jobs.PushJobWindow;

public class MainClass {

	public static void main(String[] args) {
		// display the login window as a dialog
		LoginWindow login = new LoginWindow();
		login.open();
		login = null;

		// AppSettingsWindow settings = new AppSettingsWindow();
		// settings.open();

		// String[] ui = new String[] {"a", "b", "c"};
		// boolean[] uj = new boolean[] {true, true, true, true, true, true,
		// true};
		// GadgetWindow gadget = new GadgetWindow(ui, uj);
		// gadget.open();

		// Display d = new Display();
		// JobManager jobs = new JobManager("Coddo", new String[] {
		// "Traducator",
		// "Verificator", "Encoder" });
		// jobs.createJob(
		// "AAAAAAAAAAAAAAAGASLGASNGLJKAGLJKASGKLASGNASLJKGHASLJGASLGHASLGJSAGHLJAGJLASGNJASGNKASJNGKASNGJKASNGKJASNGKASJGNASKJGNASJKGNAS",
		// 0, "WTF 1", Job.DEFAULT_NEXT_STAFF, "Settings.xml",
		// new String[] { "Settings.xml", "README.md" });
		// jobs.createJob("IOI MA 3", 0, "NA TESTARE ACI", "NBI",
		// "Settings.xml", new String[] { "Settings.xml", "README.md" });
		// jobs.createJob("IOIAFASF 4", 0, "WTF 3", Job.DEFAULT_NEXT_STAFF,
		// "Settings.xml", new String[] { "Settings.xml", "README.md" });
		// jobs.dispose();
		// d.dispose();

		// JobManager model = new JobManager("Coddo",
		// new String[] { "aaa", "bbbb" });

		// CreateJobWindow c = new CreateJobWindow();
		// c.getController().setModel(model);
		// model.addObserver(c);
		// c.open();

		// Job job = new Job();
		// job.setName("AFUHASGIOHASGAAFUHASGIOHASGAAFUHASGIOHASGAAFUHASGIOHASGAAFUHASGIOHASGAAFUHASGIOHASGA");

		// PushJobWindow push = new PushJobWindow(job);
		// push.getController().setModel(model);
		// model.addObserver(push);
		// push.open();

		// ApplicationInformation info = new ApplicationInformation();
		// info.open();

		CustomWindow.DEFAULT_FONT.dispose();
		Display.getCurrent().dispose();
	}
}