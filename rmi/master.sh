printf "
\n\n\n\n##################################################################
\n# Create RM server ons lab1-4,5,6
\nssh `whoami`@lab1-4.cs.mcgill.ca bash -s < ./cleanup.sh
\nssh `whoami`@lab1-4.cs.mcgill.ca bash -s < ./servercode/create_server.sh
\nssh `whoami`@lab1-5.cs.mcgill.ca bash -s < ./cleanup.sh
\nssh `whoami`@lab1-5.cs.mcgill.ca bash -s < ./servercode/create_server.sh
\nssh `whoami`@lab1-6.cs.mcgill.ca bash -s < ./cleanup.sh
\nssh `whoami`@lab1-6.cs.mcgill.ca bash -s < ./servercode/create_server.sh
\n
\n# Create Middleware server on lab1-7
\nssh `whoami`@lab1-7.cs.mcgill.ca bash -s < ./cleanup.sh
\nssh `whoami`@lab1-7.cs.mcgill.ca bash -s < ./servercode/create_middleware.sh
\n
\n# Create Client Server on local instance
\nssh `whoami`@lab1-1.cs.mcgill.ca bash -s < ./cleanup.sh
\nssh `whoami`@lab1-1.cs.mcgill.ca bash -s < ./servercode/create_middleware.sh
\n\n\n\n##################################################################
"