"use strict";

module.exports = function(grunt) {

	// Load grunt tasks automatically
	require("load-grunt-tasks")(grunt);
	grunt.loadNpmTasks("main-bower-files");

	/*
	 * Time how long grunt tasks take to run, this might be important when
	 * having complex builds that take forever. For now just to show how fancy
	 * grunt is.
	 */
	require("time-grunt")(grunt);

	grunt.cacheMap = [];
	// init required configurations for each task.
	grunt.initConfig({
		// Project settings
		config : {
			path : {
				webapp : {
					root : "ionic-src/",
					components : "www/js/lib/auto"
				}
			}
		},

		// From grunt-contrib-clean
		clean : {
			bowerComponents : [ "<%= config.path.webapp.root %><%= config.path.webapp.components %>" ]
		},

		// From grunt-bower-install-simple. Downloads the web
		// dependencies.
		"bower-install-simple" : {
			install : {
				options : {
					production : false
				}
			}
		},
		
		//install only the main file from web dependencies
	    bower: {
	        install: {
	        	base: "bower_components",
	            dest: "<%= config.path.webapp.root %><%= config.path.webapp.components %>",
	            options: {
	                checkExistence: true
	            }
	        }
	    },

		copy : {
			bowerComponents : {
				expand: true,
				cwd: "bower_components",
				src: "**",
				dest: "<%= config.path.webapp.root %><%= config.path.webapp.components %>"
			}
		},
	});
	
	grunt.registerTask("install", [
	       "clean:bowerComponents",
	       "bower-install-simple:install",
		   "copy:bowerComponents"])

	grunt.registerTask("default", [ "install" ]);
};