package com.example.administrator.common.widget;

import java.io.Serializable;

/**
 * Created by ZDY
 * on 2017/11/10
 */

public class Medicine  implements Serializable{

        int id; // 数据的ID

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getUseTime() {
            return useTime;
        }

        public void setUseTime(String useTime) {
            this.useTime = useTime;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getTaboo() {
            return taboo;
        }

        public void setTaboo(String taboo) {
            this.taboo = taboo;
        }

        public String getEffect() {
            return effect;
        }

        public void setEffect(String effect) {
            this.effect = effect;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        String name; // 药品名称
        String detail;
        String useTime;
        String dosage;
        String taboo;
        String effect;

        boolean isSelect; // 是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Medicine medicine = (Medicine) o;

            return name != null ? name.equals(medicine.name) : medicine.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

}
