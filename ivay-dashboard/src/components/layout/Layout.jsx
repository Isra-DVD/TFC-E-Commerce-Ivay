import React from "react";
import { Box, Container } from "@mui/material";
import Header from "./Header";
import { colors, layout as LayoutSettings } from "../../constants/styles";

/**
 * Provides the basic page layout structure including the header and a content 
 * area.
 * @param {object} props - The component props.
 * @param {React.ReactNode} props.children - The content to be rendered within 
 * the main container.
 */
const Layout = ({ children }) => {
    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                minHeight: "100vh",
                backgroundColor: colors.background.default,
            }}
        >
            <Header />
            <Container
                component="main"
                maxWidth={LayoutSettings.containerMaxWidth || "xl"}
                sx={{
                    flexGrow: 1,
                    py: { xs: 2, sm: 3 },
                }}
            >
                {children}
            </Container>
        </Box>
    );
};

export default Layout;