TcaiPopupWindows
================

TcaiPopupWindows is a small android library to create PopupWindows dialog.

提供3种组件

* 按menu键弹出的系统菜单
* 高仿UC浏览器主菜单
* 高仿UC浏览器tab列表

组件耦合度低，且这些组件支持自定义字体功能

![Example Image](http://lovefawn.github.io/TcaiPopupWindows/images/sysmenu.jpeg)  ![Example Image](http://lovefawn.github.io/TcaiPopupWindows/images/popmenu.jpeg)  ![Example Image](http://lovefawn.github.io/TcaiPopupWindows/images/urlsmenu.jpeg)

How to Use
==========

	# show sysMenu
	 
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sysMenu.show(v);
			}
		});

	# show popMenu
		
		Button btn2 = (Button) this.findViewById(R.id.btn2);
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popMenu.show(v);
			}
		});
		
	# show urllist
		
		Button btn3 = (Button) this.findViewById(R.id.btn3);
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});


Developed By
============

* lovefawn - <gwshuai@gmail.com>

Changes
=======

See [CHANGELOG](https://github.com/lorensiuswlt/NewQuickAction3D/blob/master/CHANGELOG.md) for details

License
=======

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.