package cn.varin.varaiagent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)

public abstract class ReActAgent extends BaseAgent {

    /**
     * 思考
     * @return
     */
    public abstract Boolean think();

    /**
     * 行动
     * @return
     */
    public abstract String act();

    @Override
    public String step() {
      try {
          Boolean thinkStatus = think();
          if (!thinkStatus) { //
              return "思考完成，无需行动";
          }
          return act();
      }catch (Exception e){
          e.printStackTrace();
          return "reAct执行失败："+e.getMessage();
      }
    }
}
