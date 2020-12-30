package fu.meter.idea;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowEP;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.helper.PluginHelper;
import io.zhile.research.intellij.ier.tw.MainToolWindowFactory;
import io.zhile.research.intellij.ier.ui.dialog.MainDialog;

/**
 * @desc 重置idea试用时间
 * @author meter
 * @date 2020/12/30 11:09
 */
public class ResetTrial extends AnAction {

    public ResetTrial() {
        super(Constants.ACTION_NAME, "Reset my IDE trail information.", AllIcons.General.Reset);

        AnAction optionsGroup = ActionManager.getInstance().getAction("WelcomeScreen.Options");
        if ((optionsGroup instanceof DefaultActionGroup)) {
            ((DefaultActionGroup) optionsGroup).add(this);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        //查看试用时间是否快到期，到期则提醒
        NotificationHelper.checkAndExpire(e);

        if (project == null) {
            MainDialog mainDialog = new MainDialog(Constants.ACTION_NAME);
            mainDialog.show();

            return;
        }

        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(Constants.ACTION_NAME);
        if (null == toolWindow) {
            ToolWindowEP ep = new ToolWindowEP();
            ep.id = Constants.ACTION_NAME;
            ep.anchor = ToolWindowAnchor.BOTTOM.toString();
            ep.icon = "AllIcons.General.Reset";
            ep.factoryClass = MainToolWindowFactory.class.getName();
            ep.setPluginDescriptor(PluginHelper.getPluginDescriptor());
            ToolWindowManagerEx.getInstanceEx(project).initToolWindow(ep);

            toolWindow = ToolWindowManager.getInstance(project).getToolWindow(Constants.ACTION_NAME);
        }

        toolWindow.show(null);
    }
}
