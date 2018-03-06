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
					root : "src/main/webapp/",
					components : "resources/js/lib"
				},
				target : {
					tmp : "target/tmp/",
					root : "target/grunt/",
					final : "target/hce-extranet/"
				}
			}
		},

		// From grunt-contrib-clean
		clean : {
			all : [ 
	         	"<%= config.path.webapp.root %><%= config.path.webapp.components %>",
	         	"<%= config.path.target.root %>",
	         	"<%= config.path.target.tmp %>",
	         	"<%= config.path.target.final %><%= config.path.webapp.components %>",
				"<%= config.path.target.final %>/css/*",
				"<%= config.path.target.final %>/js/*",
				"<%= config.path.target.final %>/partials/*",
				"<%= config.path.target.final %>/img/*",
				"<%= config.path.target.final %>/fonts/*",
				"<%= config.path.target.final %>/index.html"],
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

		// From grunt-wiredep. Automatically inject Bower components
		// into the HTML file
		wiredep : {
			inject : {
				directory : "<%= config.path.webapp.root %><%= config.path.webapp.components %>",
				src : [ "<%= config.path.webapp.root %>index.html" ]
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
				dot: true,
				dest: "<%= config.path.webapp.root %><%= config.path.webapp.components %>"
			},
			toTarget : {
				expand : true,
				cwd : "<%= config.path.webapp.root %>",
				src : ["**/*"],
				dest : "<%= config.path.target.root %>"
			},
			copyToFinal : {
				expand : true,
				cwd : "<%= config.path.target.root %>",
				src : [ "**/*" ],
				dest : "<%= config.path.target.final %>"
			}
		},

		//prepare configuration for concat/uglify/cssmin
		useminPrepare : {
			html : ["<%= config.path.target.root %>index.html"],
			options : {
				root : "<%= config.path.target.root %>",
				dest : "<%= config.path.target.root %>",
				staging : "<%= config.path.target.tmp %>",
				flow: {
					steps: {
						js: ["concat", "uglifyjs"],
						css: ["cssmin"]
					},
				    post: {
				        css: [{
				            name: "cssmin",
				            createConfig: function (context, block) {
  				              var generated = context.options.generated;
				              generated.options = {
				                rebase: true, // defaults to false for some unfathomable reason
				              };
				           }
				        }]
				    }
				}
			}
		},

		// avoid cache between version
		filerev : {
			options : {
				encoding : "utf8",
				algorithm : "md5",
				length : 8
			},
			generated : {
				src : [ "<%= config.path.target.root %>**", "!<%= config.path.target.root %>index.html" ],
			}
		},

		//process file for path replacement
		usemin : {
			html : "<%= config.path.target.root %>**/*.html",
			css: "<%= config.path.target.root %>**/*.css",
			js : "<%= config.path.target.root %>**/*.js",
	        options: {
	            assetsDirs: ["<%= config.path.target.root %>", "<%= config.path.target.root %>components/", "<%= config.path.target.root %>img/", "<%= config.path.target.root %>fonts/"],
	            patterns: {
	            	js: [ [/(partials\/.*?\.(?:html))/gm, "update partials path"]]
	            }
	        }
		},

		//html optim
		htmlmin : {
			prod : {
				options : {
					removeComments : true,
					removeCommentsFromCDATA : true,
					collapseWhitespace : true,
					conservativeCollapse : true,
					preserveLineBreaks : true,
					collapseBooleanAttributes : true,
					removeRedundantAttributes : true,
					useShortDoctype : true,
					keepClosingSlash : true,
					caseSensitive : true,
					processScripts : ["text/ng-template"]
				},
				files : [ {
					expand : true,
					cwd : "<%= config.path.target.root %>",
					src : [ "**/*.html" ],
					dest : "<%= config.path.target.root %>",
				} ]
			}
		}
	});
	
	grunt.registerTask("install", [
	       "clean:all", 
	       "bower-install-simple:install",
	       "copy:bowerComponents",
	       "wiredep:inject",
	       "clean:bowerComponents",
	       "bower:install" ])

	// Task: Build production version ready for deployment
	grunt.registerTask("build", [ 
          "clean:all", 
          "bower-install-simple:install",
          "copy:bowerComponents",
          "wiredep:inject",
          "clean:bowerComponents",
          "bower:install",
          "copy:toTarget", 
          "useminPrepare",
          "concat:generated",
          "cssmin:generated",
          "uglify:generated",
          "filerev:generated",
          "usemin",
          "htmlmin:prod",
          "copy:copyToFinal"
    ]);

	grunt.registerTask("default", [ "build" ]);
};